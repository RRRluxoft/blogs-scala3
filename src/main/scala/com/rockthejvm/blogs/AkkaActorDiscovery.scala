package com.rockthejvm.blogs

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration.*
import scala.util.Random

object AkkaActorDiscovery {

  /*
    IOT
    Sensors - actors
    Sensor controller

    (disconnect)

    Data aggregator
    Guardian

   */

  // Akka actor discovery == Receptionist

  case class SensorReading(id: String, value: Double)

  object DataAggregator {
    val serviceKey = ServiceKey[SensorReading]("dataAggregator")
    def apply(): Behavior[SensorReading] = active(Map())
    def active(latestReadings: Map[String, Double]): Behavior[SensorReading] =
      Behaviors.receive { (context, reading) =>
//        val id = reading.id
//        val value = reading.value
        val SensorReading(id, value) = reading
        val newReadings: Map[String, Double] = latestReadings + (id -> value)

        // display part
        context.log.info(s"[${context.self.path.name}] Latest readings: $newReadings")
        active(newReadings)
      }
  }

  // sensor
  trait SensorCommand
  case object SensorHeartbeat                                            extends SensorCommand
  case class ChangeDataAggregator(aggr: Option[ActorRef[SensorReading]]) extends SensorCommand

  object Sensor {
    def apply(id: String): Behavior[SensorCommand] = Behaviors.setup { context =>
      // use a msg adapter
      val receptionistSubscriber: ActorRef[Receptionist.Listing] = context.messageAdapter {
        case DataAggregator.serviceKey.Listing(set) =>
          ChangeDataAggregator(set.headOption)
      }

      // subscribe:
      context.system.receptionist ! Receptionist.Subscribe(DataAggregator.serviceKey, receptionistSubscriber)

      activeSensor(id, None)
    }

    def activeSensor(id: String, aggregator: Option[ActorRef[SensorReading]]): Behavior[SensorCommand] =
      Behaviors.receiveMessage {
        case SensorHeartbeat =>
          aggregator.foreach(_ ! SensorReading(id, Random.nextDouble() * 40))
          Behaviors.same[SensorCommand]

        case ChangeDataAggregator(newAgg) =>
          activeSensor(id, newAgg)
      }

    def controller(): Behavior[NotUsed] = Behaviors.setup { context =>
      val sensors = (1 to 10).map(i => context.spawn(Sensor(s"sensor_$i"), s"sensor_$i"))
      val logger = context.log // used so that we don't directly use context inside the lambda below
      // send heartbeats every second
      import context.executionContext
      context.system.scheduler.scheduleAtFixedRate(1.second, 1.second) { () =>
        logger.info("Heartbeat")
        sensors.foreach(_ ! SensorHeartbeat)
      }
      Behaviors.empty
    }
  }

  val guardian: Behavior[NotUsed] = Behaviors.setup { context =>
    // controller for the sensor
    context.spawn(Sensor.controller(), "controller")

    val dataAgg1 = context.spawn(DataAggregator(), "data_agg_1")
    // "publishing" is available by a key (srvice key)
    context.system.receptionist ! Receptionist.register(DataAggregator.serviceKey, dataAgg1)

    Thread.sleep(10000L)
    context.log.info(s"[guardian] Changing data aggregator")
    context.system.receptionist ! Receptionist.deregister(DataAggregator.serviceKey, dataAgg1)
    val dataAgg2 = context.spawn(DataAggregator(), "data_agg_2")
    context.system.receptionist ! Receptionist.register(DataAggregator.serviceKey, dataAgg2)

    // TODO

    Behaviors.empty
  }

  def main(args: Array[String]): Unit = {
    val system = ActorSystem(guardian, "ActorDiscovery")
    import system.executionContext
    system.scheduler.scheduleOnce(20 seconds, () => system.terminate())

  }
}
