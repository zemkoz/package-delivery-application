package cz.zemkoz.excercise.packagedelivery.service;

import cz.zemkoz.excercise.packagedelivery.domain.PriceListItem;
import cz.zemkoz.excercise.packagedelivery.exception.LoadPriceListFailedException;
import cz.zemkoz.excercise.packagedelivery.exception.ParseStringFailedException;
import cz.zemkoz.excercise.packagedelivery.parser.StringParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public final class DeliveryPriceListServiceImpl implements DeliveryPriceListService {
    private static final Comparator<PriceListItem> PRICE_LIST_ITEM_COMPARATOR =
            Comparator.comparingDouble(PriceListItem::getWeight).reversed();

    private final StringParser<PriceListItem> priceListItemStringParser;
    private Set<PriceListItem> priceListSet;

    public DeliveryPriceListServiceImpl(StringParser<PriceListItem> priceListItemStringParser) {
        this.priceListItemStringParser = priceListItemStringParser;
        this.priceListSet = new HashSet<>();
    }

    @Override
    public void loadPriceListFromFile(Path path) {
        try {
            priceListSet = Files.readAllLines(path).stream()
                    .map(priceListItemStringParser::parse)
                    .collect(Collectors.toCollection(() -> new TreeSet<>(PRICE_LIST_ITEM_COMPARATOR)));
        } catch (IOException | ParseStringFailedException e) {
            throw new LoadPriceListFailedException("Read price list from file on path=\"" + path + "\" failed.");
        }
    }

    @Override
    public Optional<BigDecimal> getPriceByWeight(double postPackageWeight) {
        if (priceListSet == null || priceListSet.isEmpty()) {
            return Optional.empty();
        }

        return priceListSet.stream()
                .filter(priceListItem -> priceListItem.getWeight() <= postPackageWeight)
                .map(PriceListItem::getPrice)
                .findFirst();
    }
}
