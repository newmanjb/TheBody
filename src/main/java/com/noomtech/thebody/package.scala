package com.noomtech


package object thebody {

  /**
   * Represents the oxygen that's passed around the body
   * @param amountVal The amount of Oxygen that this instance should represent
   */
  sealed case class Oxygen(amountVal : Int) {

    def amount : Int = amountVal

    def apply(amount : Int) = new Oxygen(amount)

    override def toString : String = {
      super.toString + "O2"
    }
  }

  /**
   * Represents an Integer percentage value i.e. it can only be in the range of
   * 0 to 100% where the percentage value must be a whole number
   */
  object IntegerPercentage {

    private val PERCENTAGE_CHAR = "%"
    private val PERCENTAGE_CHAR_HASHCODE = PERCENTAGE_CHAR.hashCode

    private var list: List[IntegerPercent] = List()
    for (range <- 0 to 100) {
      list = new IntegerPercent(range) :: list
    }

    def apply(percent: Int): IntegerPercent = {
      if (percent > (list.size - 1) || percent < 0) {
        throw PercentOutOfRangeException("Value " + percent + " is invalid.  " +
          "Value must be in the range of 0 to 100 inclusive");
      }
      list(100 - percent)
    }

    case class PercentOutOfRangeException(message: String) extends Exception(message) {
    }

    /**
     * Represents the actual percent value
     * @param percentVal
     */
    sealed case class IntegerPercent(percentVal: Int) {

      private val multiplier: Double = (percentVal.asInstanceOf[Double] / 100)
      private val hashCodeVal = percentVal.hashCode + PERCENTAGE_CHAR_HASHCODE

      override def toString(): String = {
        percentValue + PERCENTAGE_CHAR
      }

      private def percentValue(): Int = percentVal

      def +(other: IntegerPercent): IntegerPercent = {
        new IntegerPercent(percentValue + other.percentValue)
      }

      def -(other: IntegerPercent): IntegerPercent = {
        new IntegerPercent(percentValue - other.percentValue)
      }

      def *(other: Double): Double = {
        other * multiplier
      }

      def >(other: Long): Boolean = {
        percentValue > other
      }

      def <(other: Long): Boolean = {
        percentValue < other
      }

      def >(other: IntegerPercent): Boolean = {
        percentValue > other.percentValue
      }

      def <(other: IntegerPercent): Boolean = {
        percentValue < other.percentValue
      }

      def <=(other: Long): Boolean = {
        percentValue <= other
      }

      def <=(other: IntegerPercent): Boolean = {
        percentValue <= other.percentValue
      }

      def >=(other: Long): Boolean = {
        percentValue >= other
      }

      def >=(other: IntegerPercent): Boolean = {
        percentValue >= other.percentValue
      }

      override def hashCode: Int = {
        hashCodeVal
      }

      override def equals(other: Any): Boolean = {
        other match {
          case IntegerPercent(x) => x.equals(percentVal)
          case _ => false
        }
      }
    }
  }
}