package cz.zemkoz.excercise.packagedelivery.service;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Optional;

public interface DeliveryPriceListService {
    void loadPriceListFromFile(Path path);

    Optional<BigDecimal> getPriceByWeight(double postPackageWeight);
}
