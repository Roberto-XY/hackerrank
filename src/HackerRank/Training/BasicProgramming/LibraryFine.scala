package HackerRank.Training.BasicProgramming

import java.io.{ByteArrayInputStream, IOException, PrintWriter}
import java.util.InputMismatchException

import scala.language.higherKinds

/**
  * Copyright (c) 2017 A. Roberto Fischer
  *
  * @author A. Roberto Fischer <a.robertofischer@gmail.com> on 5/11/2017
  */
object LibraryFine {
  private val INPUT = "28 2 2015\n15 4 2015"

  //------------------------------------------------------------------------------------------//
  // Solution                                                                
  //------------------------------------------------------------------------------------------//
  private def solve(): Unit = {
    val actual = (nextInt(), nextInt(), nextInt())
    val expected = (nextInt(), nextInt(), nextInt())
    val result = (actual, expected) match {
      case ((actualDay, actualMonth, actualYear), (expectedDay, expectedMonth, expectedYear)) =>
        if (actualDay <= expectedDay && actualMonth <= expectedMonth && actualYear <= expectedYear
          || actualDay > expectedDay && actualMonth < expectedMonth && actualYear <= expectedYear
          || (actualDay > expectedDay || actualMonth > expectedMonth) && actualYear < expectedYear) {
          0
        }
        else if (actualDay > expectedDay && actualMonth == expectedMonth && actualYear == expectedYear) {
          15 * (actualDay - expectedDay)
        }
        else if (actualMonth > expectedMonth && actualYear == expectedYear) {
          500 * (actualMonth - expectedMonth)
        }
        else {
          10000
        }
    }
    println(result)
  }

  //------------------------------------------------------------------------------------------//
  // Input-Output                                                                 
  //------------------------------------------------------------------------------------------//
  private var in: java.io.InputStream = _
  private var out: java.io.PrintWriter = _

  private def println(x: Any) = out.println(x)

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    run()
  }

  @throws[Exception]
  def run(): Unit = {
    in = if (INPUT.isEmpty) System.in else new ByteArrayInputStream(INPUT.getBytes)
    out = new PrintWriter(System.out)

    val s = System.currentTimeMillis
    solve()
    out.flush()
    if (!INPUT.isEmpty) System.out.println(System.currentTimeMillis - s + "ms")
  }

  private def nextInt(): Int = {
    var num = 0
    var b = 0
    var minus = false
    while ( {
      b = readByte()
      b != -1 && !((b >= '0' && b <= '9') || b == '-')
    }) {}
    if (b == '-') {
      minus = true
      b = readByte()
    }
    while (true) {
      if (b >= '0' && b <= '9') {
        num = num * 10 + (b - '0')
      } else {
        if (minus) return -num else return num
      }
      b = readByte()
    }
    throw new IOException("Read Int")
  }

  private val inputBuffer = new Array[Byte](1024)
  var lenBuffer = 0
  var ptrBuffer = 0

  private def readByte(): Int = {
    if (lenBuffer == -1) throw new InputMismatchException
    if (ptrBuffer >= lenBuffer) {
      ptrBuffer = 0
      try {
        lenBuffer = in.read(inputBuffer)
      } catch {
        case _: IOException =>
          throw new InputMismatchException
      }
      if (lenBuffer <= 0) return -1
    }
    inputBuffer({
      ptrBuffer += 1
      ptrBuffer - 1
    })
  }
}