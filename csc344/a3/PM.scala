import scala.io.StdIn.readLine
//  S  -: E$
//  E  -: T E2
//  E2 -: '|' E3
//  E2 -: NIL
//  E3 -: T E2
//  T  -: F T2
//  T2 -: F T2
//  T2 -: NIL
//  F  -: A F2
//  F2 -: '?' F2
//  F2 -: NIL
//  A  -: C
//  A  -: '(' A2
//  A2 -: E ')'

object positionOf {
  var index = 0
}

//  S  -: E$
abstract class S {
  def eval(input: String): Boolean
}

abstract class A extends S

//  E  -: T E2
case class E(left: T, right: Option[E2]) extends S {
  def eval(input: String): Boolean = {
    val i = positionOf.index
    val a1: Boolean = left.eval(input)
    if (!a1) {
      positionOf.index = i
      right match {
        case Some(r) => r.eval(input)
        case None => a1
      }

    } else true
  }
}
//  E2 -: '|' E3
case class E2(left: E3) extends S {
  def eval(input: String): Boolean = left.eval(input)
}

//  E3 -: T E2
case class E3(left: T, right: Option[E2]) extends S {
  def eval(input: String): Boolean = {
    val i = positionOf.index
    val a1: Boolean = left.eval(input)
    if (!a1) {
      positionOf.index = i
      right match {
        case Some(r) => r.eval(input)
        case None => a1
      }

    } else true
  }
}

//  T  -: F T2
case class T(left: F, right: Option[T2]) extends S {
  def eval(input: String): Boolean = {

    val a1: Boolean = left.eval(input)
    right match {
      case Some(r) => a1 && r.eval(input)
      case None => a1
    }
  }
}

//  T2 -: F T2
case class T2(left: F, right: Option[T2]) extends S {
  def eval(input: String): Boolean = {
    val a1: Boolean = left.eval(input)
    right match {
      case Some(r) => a1 && r.eval(input)
      case None => a1
    }
  }
}

//  F  -: A F2
case class F(left: A, right: Option[F2]) extends S {
  def eval(input: String): Boolean = {
    val a1: Boolean = left.eval(input)
    //println(input(positionOf.index))
   // println(left)
    /*    if(a1 && (input(positionOf.index-1) == input(positionOf.index))){
          positionOf.index = positionOf.index-1
        }*/
    right match {
      case Some(r) => r.eval(input)
      case None => a1
    }
  }
}

//  F2 -: '?' F2
case class F2(left: Option[F2]) extends S {
  def eval(input: String): Boolean = {
    left match {
      case Some(r) => r.eval(input)
      case None => true
    }
  }
}

//  F2 -: NIL
case class A2(left: E) extends A {
  def eval(input: String): Boolean = left.eval(input)
}

//  A  -: C
case class C(left: Char) extends A {
  def eval(input: String): Boolean = {
    val matching_char = true
    if (positionOf.index < input.length && (left == '.' || left == input(positionOf.index))) {
      positionOf.index += 1
      matching_char
    }else !matching_char
  }
}

//  A  -: '(' A2
//  A2 -: E ')'
case class RecursiveDescent(input: String) {

  var index = 0
  def parseE(): E = {
    E(parseT(), parseE2())

  }

  def parseT(): T = T(parseF(), parseT2())

  def parseE2(): Option[E2] = {
    if (index < input.length && input(index) == '|') {
      index += 1; // Advance past +
      Some(E2(parseE3()))
    }
    else None
  }

  def parseE3(): E3 = E3(parseT(), parseE2())

  def parseF(): F = F(parseA(), parseF2())

  def parseT2(): Option[T2] = {
    if (index < input.length &&
      (input(index).isLetterOrDigit
        || input(index) == '.'
        || input(index) == ' '
        || input(index) == '(')) {
      Some(T2(parseF(), parseT2()))
    }
    else None
  }

  def parseF2(): Option[F2] = {
    if (index < input.length && input(index) == '?') {
      index += 1; // Advance past +
      Some(F2(parseF2()))
    }
    else None
  }

  def parseA(): A = {
    if (index < input.length && (input(index).isLetterOrDigit || input(index) == '.' || input(index) == ' ')) {
      index += 1
      C(input(index - 1))
    } else if (index < input.length && input(index) == '(') {
      index += 1
      parseA2()
    } else throw new RuntimeException("( Expected)")
  }

  def parseA2(): A2 = {
    val a = A2(parseE())
    if (!(index < input.length && input(index) == ')')) {
      throw new RuntimeException("Expected )")
    } else {
      index += 1
      a
    }
  }
}

object PM {
  def main(args: Array[String]) {
    val pattern : String = readLine("Pattern? ")
    val rd = RecursiveDescent(pattern)
    val exp2rd: S = rd.parseE()
    //println(exp2rd)
    var input = readLine("String? ")
    while (input != "exit") {
      positionOf.index = 0
      if (exp2rd.eval(input) && !(positionOf.index < input.length)){
          println("match")
        } else {
          println("no match")
        }
      input = readLine("String? :")
    }
    }
}


//Test 1
//pattern? ((h|j)ell. worl?d)|(42)
//string? hello world
//match
//string? jello word
//match
//string? jelly word
//match
//string? 42
//match
//string? 24
//no match
//string? hello world42
//no match

//Test 2
//pattern? I (like|love|hate)( (cat|dog))? people
//string? I like cat people
//match
//string? I love dog people
//match
//string? I hate people
//match
//string? I likelovehate people
//no match
//string? I people
//no match