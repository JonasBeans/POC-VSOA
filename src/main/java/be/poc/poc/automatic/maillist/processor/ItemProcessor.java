package be.poc.poc.automatic.maillist.processor;

import be.poc.poc.automatic.maillist.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemProcessor implements org.springframework.batch.item.ItemProcessor<Item, Item> {
    @Override
    public Item process(Item item) throws Exception {
        log.info("Sending mail to: {}", item.getEmail());
        return item;
    }
}
