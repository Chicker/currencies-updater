package ru.chicker.tofuext

import monix.eval.Task

package object instances {

  object monixtask {

    implicit object RaceExtForTask extends RaceExt[Task] {
      def race[A, B](fa: Task[A], fb: Task[B]): Task[Either[A, B]] = Task.race(fa, fb)

      def raceMany[A](fas: Iterable[Task[A]]): Task[A] = {
        Task.raceMany(fas)
      }
    }
  }
}
