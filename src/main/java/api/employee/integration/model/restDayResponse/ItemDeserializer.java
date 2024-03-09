package api.employee.integration.model.restDayResponse;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemDeserializer extends JsonDeserializer<List<Item>> {
    @Override
    public List<Item> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        List<Item> items = new ArrayList<>();
        if (node.isArray()) {
            for (JsonNode objNode : node) {
                items.add(mapper.treeToValue(objNode, Item.class));
            }
        } else {
            items.add(mapper.treeToValue(node, Item.class));
        }
        return items;
    }
}
