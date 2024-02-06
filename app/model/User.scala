package model

import play.api.libs.json.{Json, OFormat}

import java.util.Date

case class User (
                login: String,
                id: String,
                created_ad: Date,
                location: String,
                followers: Int,
                following: Int
                )

object User {
  implicit val formats: OFormat[User] = Json.format[User]
}