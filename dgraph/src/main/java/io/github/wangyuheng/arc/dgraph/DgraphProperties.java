package io.github.wangyuheng.arc.dgraph;

import io.dgraph.DgraphGrpc;
import io.grpc.ManagedChannelBuilder;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * External configuration properties for {@link io.grpc.ManagedChannelBuilder}.
 */
public class DgraphProperties {
    private static final String URL_SPLIT_SYMBOL = ":";

    /**
     * a list with the dgraph's host and port number.
     */
    private List<String> urls;

    private List<Address> addresses = new ArrayList<>();

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        Assert.notEmpty(urls, "urls must be not empty");
        urls.forEach(url -> {
            Assert.isTrue(url.contains(URL_SPLIT_SYMBOL), () -> "Url '" + url + "' not contains '" + URL_SPLIT_SYMBOL + "' ");
            String[] pair = url.split(URL_SPLIT_SYMBOL);
            try {
                int port = Integer.parseInt(pair[1]);
                addresses.add(new Address(pair[0], port));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Url '" + url + "' port '" + pair[1] + "' is not Integer");
            }
        });
        this.urls = urls;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public DgraphGrpc.DgraphStub[] getDgraphStubs(){
        return this.getAddresses().stream()
                .map(address -> ManagedChannelBuilder
                        .forAddress(address.getName(), address.getPort())
                        .usePlaintext()
                        .build())
                .map(DgraphGrpc::newStub)
                .toArray(DgraphGrpc.DgraphStub[]::new);
    }

    static class Address {
        private String name;
        private int port;

        public Address(String name, int port) {
            this.name = name;
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }
    }

}
