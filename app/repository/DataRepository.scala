package repository

import model.DataModel
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model._
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DataRepository @Inject()(
                                mongoComponent: MongoComponent
                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
  collectionName = "secondExerciseDB",
  mongoComponent = mongoComponent,
  domainFormat = DataModel.formats,
  indexes = Seq(IndexModel(
    Indexes.ascending("username")
  )),
  replaceIndexes = false
) {

  private def byID(username: String): Bson =
    Filters.and(
      Filters.equal("_id", username)
    )

  def index(): Future[Either[String, Seq[DataModel]]] =
    collection.find().toFuture().map { // this returns all items in the data repository, as no filters as parameters are passed.
      case users: Seq[DataModel] => Right(users)
      case _ => Left("None")
    }

  def create(user: DataModel): Future[Option[DataModel]] = {
    collection.find(byID(user.username)).headOption().flatMap {
      case Some(user: DataModel) => Future(None)
      case _ => collection.insertOne(user).toFuture().map(_ => Some(user))
    }
  }

}
