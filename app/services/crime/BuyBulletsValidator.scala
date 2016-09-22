package services.crime

import model.User

case class BuyBulletsValidator(user: User, amount: Int) {
  private def validateAmount = {
    amount match {
      case i if i <= 0    => Left("You have to buy an amount of bullets greater than zero!")
      case i if i > 1000 => Left("You can only buy 1000 bullets!")
      case _             => Right(true)
    }
 }

  private def validateUserHasMoney = {
    val totalPrice = amount * 30 // 30 is the price per bullet, make this a service later
    if (user.money > totalPrice) {
      Right(totalPrice)
    } else {
      Left("You do not have enough cash!")
    }
  }

  def validate = for {
    correctAmount <- validateAmount.right
    totalPrice <- validateUserHasMoney.right
  } yield totalPrice

}