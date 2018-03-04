package com.woodpigeon.b3

import scala.concurrent.Future

trait Behaviour[E <: Entity] {
  def name(key: E#Key): String
  def create(key: E#Key): E#View
  def update(ac: E#View, update: Any): Option[E#View]
  def project(sink: Updater, key: E#Key, before: E#View, after: E#View, update: Any): Future[_]
}

//so our projection would write to the current Context. But what would happen if the network failed, for instance?
//The Context would be best as an immutable thing then, only put back to the shared variable on a successful update.


