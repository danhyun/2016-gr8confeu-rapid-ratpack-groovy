import groovy.transform.CompileStatic
import jooq.tables.records.TodoRecord
import org.jooq.*
import org.jooq.impl.DSL
import ratpack.exec.Blocking
import ratpack.exec.Operation
import ratpack.exec.Promise

import javax.sql.DataSource

import static jooq.tables.Todo.TODO

@CompileStatic
class TodoRepository {
  private final DSLContext create

  TodoRepository(DataSource ds) {
    this.create = DSL.using(ds, SQLDialect.H2)
  }

  Promise<List<TodoModel>> getAll() {
    SelectJoinStep all = create.select().from(TODO)
    return Blocking.get { all.fetchInto(TodoModel.class) }
  }

  Promise<TodoModel> getById(Long id) {
    SelectConditionStep where = create.select().from(TODO).where(TODO.ID.equal(id))
    return Blocking.get { where.fetchOne().into(TodoModel.class) }
  }

  Promise<TodoModel> add(TodoModel todo) {
    TodoRecord record = create.newRecord(TODO, todo)
    return Blocking.op(record.&store)
      .next(Blocking.op(record.&refresh))
      .map { record.into(TodoModel.class) }
  }

  Promise<TodoModel> update(Map<String, Object> todo) {
    TodoRecord record = create.newRecord(TODO, todo)
    return Blocking.op { create.executeUpdate(record) }
      .next(Blocking.op(record.&refresh))
      .map { record.into(TodoModel.class) }
  }

  Operation delete(Long id) {
    DeleteConditionStep<TodoRecord> deleteWhereId = create.deleteFrom(TODO).where(TODO.ID.equal(id))
    return Blocking.op(deleteWhereId.&execute)
  }

  Operation deleteAll() {
    DeleteWhereStep<TodoRecord> delete = create.deleteFrom(TODO)
    return Blocking.op(delete.&execute)
  }
}
