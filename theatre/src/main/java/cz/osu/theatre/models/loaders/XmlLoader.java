package cz.osu.theatre.models.loaders;

import cz.osu.theatre.models.entities.Author;
import cz.osu.theatre.models.entities.Division;
import cz.osu.theatre.models.entities.TheatreActivity;
import cz.osu.theatre.repositories.AuthorRepository;
import cz.osu.theatre.repositories.TheatreActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cz.osu.theatre.helpers.DateAndTimeFormatter.changeDateFormat;

@Slf4j
public class XmlLoader {
    public static final String NDM_URI = "https://www.ndm.cz/cz/program/xml";

    private final NodeList nodeList;

    private final String root_node = "Item";
    private final String node_id = "id";
    private final String node_name = "name";
    private final String node_author = "author";
    private final String node_stage = "stage";
    private final String node_division = "division";
    private final String node_date = "date";
    private final String node_start = "start";
    private final String node_end = "end";
    private final String node_description = "description";
    private final String node_url = "url";

    public XmlLoader() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL(NDM_URI).openStream());

        doc.getDocumentElement().normalize();
        nodeList = doc.getElementsByTagName(root_node);
    }

    public List<Element> getElementsNodes() {
        if (nodeList == null) throw new NullPointerException("Value of nodeList in class XmlLoader is null");

        List<Element> elements = new ArrayList<>();
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                elements.add(element);
            }
        }

        return elements;
    }

    public Optional<TheatreActivity> createActivity(Element element) {
        try {
            String activity_date = changeDateFormat(element.getElementsByTagName(node_date).item(0).getTextContent(), "yyyy-MM-dd", "dd-MM-yyyy");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate date = LocalDate.parse(activity_date, formatter);

            long activity_id = Long.parseLong(element.getElementsByTagName(node_id).item(0).getTextContent());

            String activity_name = element.getElementsByTagName(node_name).item(0).getTextContent();

            String activity_stage = element.getElementsByTagName(node_stage).item(0).getTextContent();

            String activity_start = element.getElementsByTagName(node_start).item(0).getTextContent();

            String activity_end = element.getElementsByTagName(node_end).item(0).getTextContent();

            String activity_description = element.getElementsByTagName(node_description).item(0).getTextContent()
                    .replaceAll("(?m)^[ \t]*\r?\n", "");

            String activity_url = element.getElementsByTagName(node_url).item(0).getTextContent();

            TheatreActivity theatreActivity = new TheatreActivity(activity_name,activity_id, activity_stage,
                    date, activity_start, activity_end, activity_description, activity_url, 0
            );

            return Optional.of(theatreActivity);

        } catch (Exception exception) {
            log.error(String.format("Error during saving TheatreActivity with id %s",
                    Long.valueOf(element.getElementsByTagName(node_id).item(0).getTextContent())));
        }

        return Optional.empty();
    }

    public Set<Author> createAuthors(Element element) {
        Set<Author> authorsSet = new HashSet<>();
        String authors = element.getElementsByTagName(node_author).item(0).getTextContent();

        String splitLetter = ",";

        if (authors != null && !authors.trim().isEmpty()) {
            if (authors.contains(splitLetter)) {
                List<String> authorSplit = Arrays.stream(authors.split(splitLetter)).toList();
                authorSplit.forEach((author) -> addAuthor(author.trim(), authorsSet));
            } else {
                addAuthor(authors.trim(), authorsSet);
            }
        }

        return authorsSet;
    }

    public Set<Division> createDivisions(Element element) {
        Set<Division> divisionsSet = new HashSet<>();
        String divisions = element.getElementsByTagName(node_division).item(0).getTextContent();

        String splitLetter = "/";

        if (divisions != null && !divisions.trim().isEmpty()) {
            if (divisions.contains(splitLetter)) {
                List<String> authorSplit = Arrays.stream(divisions.split(splitLetter)).toList();
                authorSplit.forEach((author) -> addDivision(author.trim(), divisionsSet));
            } else {
                addDivision(divisions.trim(), divisionsSet);
            }
        }

        return divisionsSet;
    }

    private void addDivision(String division, Set<Division> divisionSet) {
        Division newDivision = new Division(division);
        divisionSet.add(newDivision);
    }

    private void addAuthor(String author, Set<Author> authorsSet) {
        String whiteSpace = " ";
        Author newAuthor;

        if (author.contains(whiteSpace)){
            String firstName = author.substring(0,author.indexOf(whiteSpace));
            String lastName = author.substring(author.indexOf(whiteSpace));
            newAuthor = new Author(firstName,lastName);
        }else{
            newAuthor = new Author(author);
        }

        authorsSet.add(newAuthor);
    }

    public long getElementId(Element element) {
        return Long.parseLong(element.getElementsByTagName(node_id).item(0).getTextContent());
    }
}
