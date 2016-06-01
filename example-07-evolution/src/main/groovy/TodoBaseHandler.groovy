import groovy.transform.CompileStatic
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.InjectionHandler
import ratpack.http.Response
import ratpack.jackson.Jackson

@CompileStatic
class TodoBaseHandler extends InjectionHandler { // <1>
  void handle(Context ctx, TodoRepository repository) throws Exception { // <2>
    Response response = ctx.response
    ctx.byMethod({ ByMethodSpec method -> method
      .options {
        response.headers.set('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE')
        response.send()
      }
      .get {
        repository.all
          .map(Jackson.&json)
          .then(ctx.&render)
      }
      .post {
        Promise<TodoModel> todo = ctx.parse(Jackson.fromJson(TodoModel))
        todo
          .flatMap(repository.&add)
          .map(Jackson.&json)
          .then(ctx.&render)
      }
      .delete { repository.deleteAll().then(response.&send) }
    } as Action<ByMethodSpec>)
  }
}
