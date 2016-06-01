import jooq.tables.Todo
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import ratpack.hikari.HikariModule

import javax.sql.DataSource

import ratpack.jackson.Jackson
import static ratpack.groovy.Groovy.ratpack

ratpack {
  bindings {
    module(HikariModule) { config ->
      config.dataSourceClassName = 'org.h2.jdbcx.JdbcDataSource'
      config.addDataSourceProperty('URL', "jdbc:h2:mem:tood;INIT=RUNSCRIPT FROM 'classpath:/init.sql'") // <3>
    }
  }
  handlers {
    all(new CORSHandler())
    // tag::jooq-1[]
    get('blocking') {
      DataSource ds = get(DataSource) // <1>
      DSLContext dsl = DSL.using(ds, SQLDialect.H2) // <2>
      def todos = dsl.select().from(Todo.TODO).fetchMaps() // <3>
      render(Jackson.json(todos)) // <4>
    }
    // end::jooq-1[]
  }
}
