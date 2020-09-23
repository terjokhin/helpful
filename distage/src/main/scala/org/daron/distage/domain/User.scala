package org.daron.distage.domain

import derevo.derive
import io.circe.Codec
import tofu.logging.derivation.loggable

@derive(loggable)
final case class User(id: String, firstname: String, lastname: String)

object User {
  implicit val codec: Codec[User] = io.circe.generic.semiauto.deriveCodec[User]
}
