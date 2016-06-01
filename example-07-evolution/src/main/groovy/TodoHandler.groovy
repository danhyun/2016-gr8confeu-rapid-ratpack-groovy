import com.google.common.reflect.TypeToken
import groovy.transform.CompileStatic
import ratpack.exec.Promise
import ratpack.func.Action
import ratpack.func.Function
import ratpack.handling.ByMethodSpec
import ratpack.handling.Context
import ratpack.handling.InjectionHandler
import ratpack.http.Response
import ratpack.jackson.Jackson
import ratpack.jackson.JsonRender

@CompileStatic
class TodoHandler extends InjectionHandler {
  void handle(Context ctx, TodoRepository repo, String base) throws Exception {
    Long todoId = Long.parseLong(ctx.pathTokens.get('id'))

    Function<TodoModel, TodoModel> hostUpdater = { TodoModel todo -> todo.baseUrl(base) } as Function<TodoModel, TodoModel>
    Function<TodoModel, JsonRender> toJson = hostUpdater.andThen { todo -> Jackson.json(todo) }

    Response response = ctx.response

    ctx.byMethod({ ByMethodSpec byMethodSpec -> byMethodSpec
      .options {
        response.headers.set('Access-Control-Allow-Methods', 'OPTIONS, GET, PATCH, DELETE')
        response.send()
      }
      .get { repo.getById(todoId).map(toJson).then(ctx.&render) }
      .patch {
        ctx
          .parse(Jackson.fromJson(new TypeToken<Map<String, Object>>() {}))
          .map({ Map<String, Object> map ->
            Map<String, Object> patch = map.keySet().inject([:]) { m, key ->
              m[key.toUpperCase()] = map[key]
              return m
            } as Map<String, Object>
            patch['ID'] = todoId
            return patch
          } as Function<Map<String, Object>, Map<String, Object>>)
          .flatMap(repo.&update as Function<Map<String, Object>, Promise<TodoModel>>)
          .map(toJson)
          .then(ctx.&render)
      }
      .delete { repo.delete(todoId).then(response.&send) }
    } as Action<ByMethodSpec>)
  }
}
