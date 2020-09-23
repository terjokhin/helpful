package org.daron.distage.db

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.functor._
import cats.tagless.{Derive, FunctorK}
import org.daron.distage.domain.User

import scala.collection.concurrent.TrieMap

trait UserRepo[F[_]] {
  def save(user: User): F[Unit]
  def find(id: String): F[Option[User]]
}

object UserRepo {

  final class MapLikeUserRepo[F[_]: Monad](data: TrieMap[String, User]) extends UserRepo[F] {
    override def save(user: User): F[Unit] = data.put(user.id, user).pure[F].void

    override def find(id: String): F[Option[User]] = data.get(id).pure[F]
  }

  implicit def functorK: FunctorK[UserRepo] = Derive.functorK[UserRepo]
}
