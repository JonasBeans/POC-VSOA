package be.poc.poc.automatic.maillist.mapper;

import be.poc.poc.automatic.maillist.model.Item;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper extends BeanWrapperFieldSetMapper<Item> {
    public ItemMapper() {
        setTargetType(Item.class);
    }
}
