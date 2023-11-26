package be.poc.poc.automatic.maillist.writer;

import be.poc.poc.automatic.maillist.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NoItemWrite implements ItemWriter<Item> {
    @Override
    public void write(Chunk<? extends Item> chunk) throws Exception {
        log.info("Writed some item");
    }
}
