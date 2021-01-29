package ru.chicker.tofuext

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import monix.eval.Task
import monix.execution.Scheduler
import scala.concurrent.duration._
import tofu.Race

class RaceTest extends AnyFunSpec with Matchers {

  implicit val sched = Scheduler.Implicits.global

  describe("race") {
    it("should fail race-task if one of the tasks was failed before any other task successfully completed") {
      val failMessage = "BOOM!"

      val task1: Task[Int] = Task(1).delayExecution(50.millis)
      val task2: Task[Int] = Task.raiseError(new Exception(failMessage))

      val s = implicitly[Race[Task]]
      val result = s.race(task1, task2)

      intercept[Exception] {
        result.runSyncUnsafe()
      }.getMessage() shouldEqual failMessage
    }
  }
}
