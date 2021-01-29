package ru.chicker.tofuext

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import monix.eval.Task
import monix.execution.Scheduler
import scala.concurrent.duration._
import ru.chicker.tofuext.RaceExt

class RaceExtTest extends AnyFunSpec with Matchers {
  import ru.chicker.tofuext.instances.monixtask._

  implicit val sched = Scheduler.Implicits.global

  describe("raceMany") {
    it("should fail race-task if one of the tasks was failed before any other task successfully completed") {
      val failMessage = "BOOM!"

      val task1: Task[Int] = Task(1)
      val task2: Task[Int] = Task.raiseError(new Exception(failMessage))

      val s = implicitly[RaceExt[Task]]
      val result = s.raceMany(orderedTasks(task2, task1))

      intercept[Exception] {
        result.runSyncUnsafe()
      }.getMessage() shouldEqual failMessage
    }
  }

  it("should successfully completed if the first completed task is successfully completed") {
      val failMessage = "BOOM!"

      val task1: Task[Int] = Task(1)
      val task2: Task[Int] = Task.raiseError(new Exception(failMessage))

      val s = implicitly[RaceExt[Task]]
      val result = s.raceMany(orderedTasks(task1, task2))

      result.runSyncUnsafe() shouldEqual 1
  }

  describe("raceManyIgnoreErrors") {
    it("should successfully completed if the one of task is completed successfully") {
      val failMessage = "BOOM!"

      val task1: Task[Int] = Task(1)
      val task2: Task[Int] = Task.raiseError(new Exception(failMessage))

      val s = implicitly[RaceExt[Task]]
      val result = s.raceManyIgnoreErrors(orderedTasks(task2, task1))

      result.runSyncUnsafe() shouldEqual 1
    }
  }

  private def orderedTasks[A](tasks: Task[A]*): Seq[Task[A]] = {
    val delay = 50.millis
    (0 to tasks.length).zip(tasks).map { case (times, t) => t.delayExecution(delay * times.toLong) }
  }
}
