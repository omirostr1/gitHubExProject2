package model

import play.api.libs.json.{Json, OFormat}
import java.util.Date

case class DataModel (
                       username: String,
                       dateAccountCreated: String,
                       location: String,
                       noOfFollowers: Int,
                       noOfFollowing: Int
                     )
object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}
