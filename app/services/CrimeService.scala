package services

object CrimeService {
  
  def crimeAmount = {
    Math.random * 1000 toInt
  }
  
}