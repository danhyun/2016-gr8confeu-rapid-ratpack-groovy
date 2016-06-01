import ratpack.hikari.HikariModule
import ratpack.jackson.Jackson
import ratpack.exec.Promise

import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {
    module(HikariModule) { config ->
      config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource'
      config.addDataSourceProperty('URL', "jdbc:h2:mem:tood;INIT=RUNSCRIPT FROM 'classpath:/init.sql'")
    }
    module(TodoModule)
    bindInstance(new CORSHandler()) // <1>
  }
  handlers {
    all(CORSHandler) // <2>
    path { TodoRepository repository -> // <3>
      byMethod {
        options {
          response.headers.set('Access-Control-Allow-Methods', 'OPTIONS, GET, POST, DELETE')
          response.send()
        }
        get {
          repository.getAll()
            .map(Jackson.&json)
            .then(context.&render)
        }
        post {
          Promise<TodoModel> todo = parse(Jackson.fromJson(TodoModel))
          todo
            .flatMap(repository.&add)
            .map(Jackson.&json)
            .then(context.&render)
        }
        delete {
          repository.deleteAll().then(response.&send)
        }
      }
    }
  }
}
