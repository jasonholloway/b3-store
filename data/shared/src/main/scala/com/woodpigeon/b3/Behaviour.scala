package com.woodpigeon.b3

trait Behaviour[E <: Entity] {
  def name(key: E#Key): String
  def create(key: E#Key): E#View
  def update(ac: E#View, update: Any): Option[E#View]
  def project(key: E#Key, ac: E#View, version: Int, update: Any): Seq[Any]
}
