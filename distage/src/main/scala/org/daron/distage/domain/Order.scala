package org.daron.distage.domain

import derevo.derive
import io.circe.Codec
import tofu.logging.derivation.loggable

@derive(loggable)
final case class Order(id: String, userId: String, price: Long)

object Order {
  implicit val codec: Codec[Order] = io.circe.generic.semiauto.deriveCodec[Order]
}
