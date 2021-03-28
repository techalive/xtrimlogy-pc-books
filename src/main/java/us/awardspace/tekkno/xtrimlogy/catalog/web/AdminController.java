package us.awardspace.tekkno.xtrimlogy.catalog.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogInitializerUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.application.port.CatalogUseCase;
import us.awardspace.tekkno.xtrimlogy.catalog.db.AuthorJpaRepository;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Author;
import us.awardspace.tekkno.xtrimlogy.catalog.domain.Book;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.OrderItemCommand;
import us.awardspace.tekkno.xtrimlogy.order.application.port.QueryOrderUseCase;
import us.awardspace.tekkno.xtrimlogy.order.domain.OrderItem;
import us.awardspace.tekkno.xtrimlogy.order.domain.Recipient;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;

import static us.awardspace.tekkno.xtrimlogy.order.application.port.ManipulateOrderUseCase.*;

@Slf4j
@RestController
@Secured({"ROLE_ADMIN"})
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final CatalogInitializerUseCase initializer;

    @PostMapping("/initialization")
    @Transactional
    public void initialize() {
        initializer.initialize();
    }

}
