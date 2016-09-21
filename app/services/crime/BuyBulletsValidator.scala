package services.crime

import model.User


case class BuyBulletsValidator(user: User, amount: Int) {
  private def validateAmount = {
    if (amount > 0 && amount <= 1000)
      Right(true)
    else Left("You have to buy an amount of bullets greater than zero and less than 1000!")
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