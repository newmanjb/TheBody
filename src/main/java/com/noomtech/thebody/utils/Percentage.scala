package com.noomtech.thebody.utils

/**
 * Represents a percentage value i.e. a value between 0 and 100
 */
object Percentage {

  val PERCENTAGE_CHAR = "%"

  val list : List[Percent] = List()
  for(range <- 0 to 100) {
    list::List(new Percent(range))
  }

  def apply (percent : Int) : Percent = {
    list(percent)
  }

  sealed class Percent(percentValue : Int) {
    override def toString() : String = {
      percentValue + PERCENTAGE_CHAR
    }

    def + (other : Percent) : Percent = {
      new Percent(percentValue + other.percentValue)
    }

    def - (other : Percent) : Percent = {
      new Percent(percentValue - other.percentValue)
    }

    def * (other : Long) : Long = {
      other * (percentValue / 100)
    }

    def > (other : Long) : Boolean = {
      percentValue > other
    }

    def < (other : Long) : Boolean = {
      percentValue < other
    }

    def > (other : Percent) : Boolean = {
      percentValue > other.percentValue
    }

    def < (other : Percent) : Boolean = {
      percentValue < other.percentValue
    }

    def <= (other : Long) : Boolean = {
      percentValue < other
    }

    def <= (other : Percent) : Boolean = {
      percentValue <= other.percentValue
    }

    def >= (other : Long) : Boolean = {
      percentValue > other
    }

    def >= (other : Percent) : Boolean = {
      percentValue >= other.percentValue
    }
  }
}