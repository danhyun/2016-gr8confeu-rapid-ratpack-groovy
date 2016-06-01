@Grab('io.ratpack:ratpack-groovy:1.3.3') // <1>

import static ratpack.groovy.Groovy.ratpack

ratpack { // <2>
  handlers { // <3>
    get { // <4>
      render 'Hej Verden' // <5>
    }
  }
}
