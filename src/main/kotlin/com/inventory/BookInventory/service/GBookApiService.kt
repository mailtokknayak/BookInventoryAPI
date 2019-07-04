package com.inventory.BookInventory.service


import com.inventory.BookInventory.model.GBookData
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Flux



@Service
class GBookApiService {

    val apiKey : String = "AIzaSyBS557-qs46hmuh_tAMJp3CeEqmoqT_d9w"
    fun gBookApi(query: String): Flux<GBookData> {
        val url  = "https://www.googleapis.com/books/v1/volumes?q=$query+intitle&key=$apiKey"

//        val restTemplate = RestTemplate()
//        return restTemplate.getForObject(url, GBookData::class.java)

        return WebClient
                .create(UriComponentsBuilder.fromHttpUrl(url)
                        .encode().toUriString())
                .get()
                .retrieve()
                .bodyToFlux(GBookData::class.java)
    }


}