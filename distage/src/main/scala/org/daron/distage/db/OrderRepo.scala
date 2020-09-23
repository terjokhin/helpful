package org.daron.distage.db

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.functor._
import org.daron.distage.domain.Order

import scala.collection.concurrent.TrieMap

trait OrderRepo[F[_]] {
  def save(order: Order): F[Unit]
  def find(id: String): F[Option[Order]]
}

object OrderRepo {

  final class MapLikeOrderRepo[F[_]: Monad](data: TrieMap[String, Order]) extends OrderRepo[F] {
    override def save(order: Order): F[Unit] = data.put(order.id, order).pure[F].void

    override def find(id: String): F[Option[Order]] = data.get(id).pure[F]
  }

}
