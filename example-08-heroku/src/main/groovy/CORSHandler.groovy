import groovy.transform.CompileStatic
import ratpack.groovy.handling.GroovyContext
import ratpack.groovy.handling.GroovyHandler
import ratpack.http.MutableHeaders
import ratpack.registry.Registry

@CompileStatic
class CORSHandler extends GroovyHandler {
  @Override
  protected void handle(GroovyContext context) {
    MutableHeaders headers = context.response.headers
    headers.set('Access-Control-Allow-Origin', '*')
    headers.set('Access-Control-Allow-Headers', 'x-requested-with, origin, content-type, accept')
    String host = context.request.headers.get('HOST')
    String baseUrl = "http://$host/todo"
    context.next(Registry.single(String, baseUrl))
  }
}
