# Ktorx Response 
Go functional cross context-boundaries with serializable generic responses. 

This package provides a KSeraializer for the generic response class
```kotlin
sealed class Response<Data> {
    data class Success<Data>(
        val data: Data
    )
    data class Failure<Data>(
        val message: String, 
        val code: Int
    ) 
}
```
together with some http-related helper functions for effective usage.  
For more details, please visit the source code.

## Important observations:
  1. Response.Failure<T> is type-agnostic. 
     This allows easy 'exception-handling' cross api boundaries; 
     The error can simply be passed back to the client through a whole sequence of requests.
  2. A serialized ```Response<T>``` can always be deserialized to a ```Response<JsonElement>``` and serialized back to ```Reaponse<T>``` again. 
     This is of interest if a ```Response<T>```, with unknown type T, has to be forwarded, e.g. on a reverse proxy. 
     From a functional perspective, reboxing should be ok provided that the information remains untouched.  

## Example (schematically): 

Server side (using ktor) :
```kotlin
data class Data( /* parameters */ )
...
routing() {
    get("/response") {
        call.respond(Json.stringify(
            Response.serializer(Data.serializer()),
            Response.Success(Data(/* parameters */))
        ))
    }
} 
...
```

client side (using ktor client):
```kotlin
@Serializable
data class Data( /* parameters */ )

val response: Response<Data> = with(HttpClient()) {
    get(
        serializer = Data.serializer()),
        url = example.com/response
}
```

where, the response class is given by
```kotlin
sealed class Response<Data> {
    data class Success<Data>(val data: Data)
    data class Failure<Data>(val message: String, val code: Int) 
}
```

