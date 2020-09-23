package org.daron.distage

import cats.Monad
import distage.{ModuleDef, TagK}
import org.daron.distage.db.OrderRepo.MapLikeOrderRepo
import org.daron.distage.db.UserRepo.MapLikeUserRepo
import org.daron.distage.domain.{Order, User}

import scala.collection.concurrent.TrieMap

package object db {

  def RepoModule[F[_]: TagK : Monad] =  new ModuleDef {
    make[OrderRepo[F]].from(new MapLikeOrderRepo[F](new TrieMap[String, Order]()))
    make[UserRepo[F]].from(new MapLikeUserRepo[F](new TrieMap[String, User]()))
    addImplicit[Monad[F]]
  }
}
