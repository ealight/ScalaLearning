package study.softserve.scala

object JSONParser {

  case class Weather(main: String, description: String) {
    override def toString: String = {
      s"(main=$main, description=$description)"
    }
  }

  case class Main(temp: Double, feelsLike: Double, tempMin: Double, tempMax: Double) {
    override def toString: String = {
      s"(temp=$temp, feelsLike=$feelsLike, tempMin=$tempMin, tempMax=$tempMax)"
    }
  }

  case class Clouds(all: Int) {
    override def toString: String = {
      s"(count=$all)"
    }
  }

  case class Sys(country: String, sunrise: Long, sunset: Long) {
    override def toString: String = {
      s"(country=$country, sunriseTime=$sunrise, sunsetTime=$sunset)"
    }
  }

  case class CityWeather(name: String, dt: Long, main: Main, weather: List[Weather], clouds: Clouds, sys: Sys, timezone: Int)
    extends Ordered[CityWeather] {
    override def toString: String = {
      s"(name=$name, date=$dt, main=$main, weather=$weather, clouds=$clouds, system=$sys, timezone=$timezone)"
    }

    override def compare(that: CityWeather): Int = {
      (this.main.temp - that.main.temp).toInt
    }
  }

}
