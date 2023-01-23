package com.rockthejvm.blogs

object TypeLambdas {


  type MyList = [T] =>> List[T]

  type MapWithStringKey = [T] =>> Map[String, T]
  type MapWithStringKey2[T] = Map[String, T]
  val addressBook: MapWithStringKey[String] = Map()

  type SpecialEither = [T, E] =>> Either[E, Option[T]]
  val specialEither: SpecialEither[Int, String] = Right(Some(42))


  // monads:
  trait Monad[M[_]] {
    def pure[A](value: A): M[A]
    def flatMap[A, B](ma: M[A])(f: A => M[B]): M[B]
    def map[A, B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(a => pure(f(a)))
  }

  // monad for either
  class EitherMonad[E] extends Monad[[T] =>> Either[E, T]] {
    override def pure[A](value: A): Either[E, A] = ???
    override def flatMap[A, B](ma: Either[E, A])(f: A => Either[E, B]): Either[E, B] = ???
  }

  @main def lambdas(): Unit = {
    val m: Monad[List] = new Monad[List] {
      override def pure[A](value: A): List[A] = List(value)

      override def flatMap[A, B](ma: List[A])(f: A => List[B]): List[B] = ??? // f(ma)
    }
  }
}
