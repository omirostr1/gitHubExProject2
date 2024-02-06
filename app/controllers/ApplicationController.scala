package controllers

import model.DataModel
import play.api.libs.json._
import play.api.mvc._
import service.RepositoryService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val repositoryService: RepositoryService)(implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    repositoryService.index().map {
      case Right(books: Seq[DataModel]) => Ok(Json.toJson(books))
      case Left(_) => Status(INTERNAL_SERVER_ERROR)(Json.toJson(s"Unable to find any books because no books are stored in the database"))
    }
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        repositoryService.create(dataModel).map {
          case Right(book: DataModel) => Created(Json.toJson(book))
          case Left(_) => Status(INTERNAL_SERVER_ERROR)(Json.toJson("Error: entry cannot be created due to duplicate id entered"))
        }
      case JsError(_) => Future(BadRequest)
    }
  }

}
