package service

import model.{APIError, DataModel}
import repository.DataRepository

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RepositoryService @Inject()(val dataRepository: DataRepository)(implicit ec: ExecutionContext) {

  def index(): Future[Either[APIError.BadAPIResponse, Seq[DataModel]]] =
    dataRepository.index().map { // this returns all items in the data repository, as no filters as parameters are passed.
      case Right(users: Seq[DataModel]) => Right(users)
      case _ => Left(APIError.BadAPIResponse(404, "Books cannot be found"))
    }

  def create(user: DataModel): Future[Either[APIError.BadAPIResponse, DataModel]] =
    dataRepository.create(user).map {
      case None => Left(APIError.BadAPIResponse(500, "Erro: entry cannot be created due to duplicate id"))
      case _ => Right(user)
    }
}
