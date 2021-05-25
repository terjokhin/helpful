package org.daron.ctxrunner

import derevo.derive
import tofu.logging.derivation.loggable

@derive(loggable)
final case class User(ip: String)
