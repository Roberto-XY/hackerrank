package HackerRank.Training.MathFundamentals

import java.io.{ByteArrayInputStream, IOException, InputStream, PrintWriter}
import java.util.InputMismatchException

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable
import scala.language.higherKinds
import scala.math.BigInt
import scala.util.control.TailCalls.{TailRec, done, tailcall}

/**
  * Copyright (c) 2017 A. Roberto Fischer
  *
  * @author A. Roberto Fischer <a.robertofischer@gmail.com> on 6/28/2017
  */
private[this] object IsFibo {

  import Reader._
  import Writer._

  private[this] val TEST_INPUT: Option[String] = None


  //------------------------------------------------------------------------------------------//
  // Solution                                                                
  //------------------------------------------------------------------------------------------//
  private[this] def solve(): Unit = {
    val maxN = BigInt("10000000000")

    val fibonacciNumbers = Stream.from(0)
      .map(n => new Fibonacci[Long].apply(n))
      .takeWhile(_ <= maxN)
      .foldLeft(Set.newBuilder[Long])(_ += _).result()

    val t = nextInt()
    next[Long, Vector](nextLong(), t).foreach { n =>
      println(
        if (fibonacciNumbers.contains(n)) "IsFibo" else "IsNotFibo"
      )
    }
  }

  private[this] final class Fibonacci[T: Integral] extends (T ==> (T, T)) {

    import Integral.Implicits._

    //  F(2n-1) = F(n)^2 + F(n-1)^2
    //  F(2n) = (2F(n-1) + F(n))*F(n)
    def _fibonacci(n: T): TailRec[(T, T)] = {
      if (n == 0) {
        done(cache.getOrElseUpdate(n, (implicitly[Integral[T]].zero, implicitly[Integral[T]].one)))
      } else {
        val (a, b) = tailcall(_fibonacci(n / implicitly[Integral[T]].fromInt(2))).result
        val c = (implicitly[Integral[T]].fromInt(2) * b - a) * a
        val d = a * a + b * b
        if (n % implicitly[Integral[T]].fromInt(2) == 0) {
          done(cache.getOrElseUpdate(n, (c, d)))
        } else {
          done(cache.getOrElseUpdate(n, (d, c + d)))
        }
      }
    }

    override def apply(v1: T): T = {
      _fibonacci(v1).result._1
    }
  }

  trait Memo[I, K, O, M] extends (I => O) {
    private[this] type Input = I
    private[this] type Key = K
    private[this] type Output = O
    private[this] type Memory = M
    val cache: mutable.Map[K, M] = mutable.Map.empty[K, M]

    override def apply(v1: I): O
  }

  type ==>[I, M] = Memo[I, I, I, M]


  //------------------------------------------------------------------------------------------//
  // Run
  //------------------------------------------------------------------------------------------//
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val s = System.currentTimeMillis
    solve()
    flush()
    if (TEST_INPUT.isDefined) System.out.println(System.currentTimeMillis - s + "ms")
  }

  //------------------------------------------------------------------------------------------//
  // Input
  //------------------------------------------------------------------------------------------//
  private[this] final object Reader {

    private[this] implicit val in: InputStream = TEST_INPUT.fold(System.in)(s => new ByteArrayInputStream(s.getBytes))

    def nextSeq[T, Coll[_]](reader: => Seq[T], n: Int)
                           (implicit cbf: CanBuildFrom[Coll[T], T, Coll[T]]): Coll[T] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (_ <- 0 until n) {
        builder ++= reader
      }
      builder.result()
    }

    def next[T, Coll[_]](reader: => T, n: Int)
                        (implicit cbf: CanBuildFrom[Coll[T], T, Coll[T]]): Coll[T] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (_ <- 0 until n) {
        builder += reader
      }
      builder.result()
    }

    def nextWithIndex[T, Coll[_]](reader: => T, n: Int)
                                 (implicit cbf: CanBuildFrom[Coll[(T, Int)], (T, Int), Coll[(T, Int)]]): Coll[(T, Int)] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (i <- 0 until n) {
        builder += ((reader, i))
      }
      builder.result()
    }

    def nextDouble[Coll[Double]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[Double], Double, Coll[Double]]): Coll[Double] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (_ <- 0 until n) {
        builder += nextDouble()
      }
      builder.result()
    }

    def nextDoubleWithIndex[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[(Double, Int)], (Double, Int), Coll[(Double, Int)]]): Coll[(Double, Int)] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (i <- 0 until n) {
        builder += ((nextDouble(), i))
      }
      builder.result()
    }

    def nextChar[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[Char], Char, Coll[Char]]): Coll[Char] = {
      val builder = cbf()
      builder.sizeHint(n)
      var b = skip
      var p = 0
      while (p < n && !isSpaceChar(b)) {
        builder += b.toChar
        p += 1
        b = readByte()
      }
      builder.result()
    }

    def nextCharWithIndex[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[(Char, Int)], (Char, Int), Coll[(Char, Int)]]): Coll[(Char, Int)] = {
      val builder = cbf()
      builder.sizeHint(n)
      var b = skip
      var p = 0
      while (p < n && !isSpaceChar(b)) {
        builder += ((b.toChar, p))
        p += 1
        b = readByte()
      }
      builder.result()
    }

    def nextInt[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[Int], Int, Coll[Int]]): Coll[Int] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (_ <- 0 until n) {
        builder += nextInt()
      }
      builder.result()
    }

    def nextIntWithIndex[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[(Int, Int)], (Int, Int), Coll[(Int, Int)]]): Coll[(Int, Int)] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (i <- 0 until n) {
        builder += ((nextInt(), i))
      }
      builder.result()
    }

    def nextLong[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[Long], Long, Coll[Long]]): Coll[Long] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (_ <- 0 until n) {
        builder += nextLong()
      }
      builder.result()
    }

    def nextLongWithIndex[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[(Long, Int)], (Long, Int), Coll[(Long, Int)]]): Coll[(Long, Int)] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (i <- 0 until n) {
        builder += ((nextLong(), i))
      }
      builder.result()
    }

    def nextString[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[String], String, Coll[String]]): Coll[String] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (_ <- 0 until n) {
        builder += nextString()
      }
      builder.result()
    }

    def nextStringWithIndex[Coll[_]]
    (n: Int)(implicit cbf: CanBuildFrom[Coll[(String, Int)], (String, Int), Coll[(String, Int)]]): Coll[(String, Int)] = {
      val builder = cbf()
      builder.sizeHint(n)
      for (i <- 0 until n) {
        builder += ((nextString(), i))
      }
      builder.result()
    }

    def nextMultiLine(n: Int, m: Int): Array[Array[Char]] = {
      val map = new Array[Array[Char]](n)
      var i = 0
      while (i < n) {
        map(i) = nextChar[Array](m)
        i += 1
      }
      map
    }

    def nextDouble(): Double = nextString().toDouble

    def nextChar(): Char = skip.toChar

    def nextString(): String = {
      var b = skip
      val sb = new java.lang.StringBuilder
      while (!isSpaceChar(b)) {
        sb.appendCodePoint(b)
        b = readByte()
      }
      sb.toString
    }

    def nextInt(): Int = {
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

    def nextLong(): Long = {
      var num = 0L
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
      throw new IOException("Read Long")
    }

    private[this] val inputBuffer = new Array[Byte](1024)
    private[this] var lenBuffer = 0
    private[this] var ptrBuffer = 0

    private[this] def readByte()(implicit in: java.io.InputStream): Int = {
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

    private[this] def isSpaceChar(c: Int) = !(c >= 33 && c <= 126)

    private[this] def skip = {
      var b = 0
      while ( {
        b = readByte()
        b != -1 && isSpaceChar(b)
      }) {}
      b
    }
  }

  //------------------------------------------------------------------------------------------//
  // Output
  //------------------------------------------------------------------------------------------//
  private[this] final object Writer {

    private[this] val out = new PrintWriter(System.out)

    def flush(): Unit = out.flush()

    def println(x: Any): Unit = out.println(x)

    def print(x: Any): Unit = out.print(x)
  }

}