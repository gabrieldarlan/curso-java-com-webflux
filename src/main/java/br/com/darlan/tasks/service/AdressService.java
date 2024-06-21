package br.com.darlan.tasks.service;

import br.com.darlan.tasks.client.ViaCepClient;
import br.com.darlan.tasks.exception.CepNotFoundException;
import br.com.darlan.tasks.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AdressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Address.class);
    private final ViaCepClient viaCepClient;

    public AdressService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public Mono<Address> getAddress(String zipCode) {
        return Mono.just(zipCode)
                .doOnNext(it -> LOGGER.info("Getting address to ZipCode {}", zipCode))
                .flatMap(viaCepClient::getAddress)
                .doOnError(it -> Mono.error(CepNotFoundException::new));
    }

}
