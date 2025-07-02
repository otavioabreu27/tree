package com.sensedia.deps.adapter.out;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import com.sensedia.deps.domain.model.Dependency;
import com.sensedia.deps.domain.model.ProjectWithDependencies;
import com.sensedia.deps.domain.port.out.PomReaderPort;

public class FilesystemPomReaderAdapter implements PomReaderPort {

    @Override
    public ProjectWithDependencies readProject(Path pomPath) {
        Dependency project = null;
        List<Dependency> dependencies = new ArrayList<>();

        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(pomPath.toFile());
            doc.getDocumentElement().normalize();

            String groupId = getTagValue(doc, "groupId");
            if (groupId == null) {
                groupId = getTagValue(doc, "parent/groupId");
            }
            String artifactId = getTagValue(doc, "artifactId");
            String version = getTagValue(doc, "version");
            if (version == null) {
                version = getTagValue(doc, "parent/version");
            }

            if (groupId != null && artifactId != null && version != null) {
                project = new Dependency(groupId + "." + artifactId, version);
            }

            NodeList depList = doc.getElementsByTagName("dependency");
            for (int i = 0; i < depList.getLength(); i++) {
                Node node = depList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) node;
                    String depGroup = getTagValue(el, "groupId");
                    String depArtifact = getTagValue(el, "artifactId");
                    String depVersion = getTagValue(el, "version");
                    if (depGroup != null && depArtifact != null && depVersion != null) {
                        dependencies.add(new Dependency(depGroup + "." + depArtifact, depVersion));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler POM: " + pomPath);
            e.printStackTrace();
        }

        return new ProjectWithDependencies(project, dependencies);
    }

    private String getTagValue(Document doc, String tagPath) {
        String[] tags = tagPath.split("/");
        Element current = doc.getDocumentElement();
        for (int i = 0; i < tags.length - 1; i++) {
            NodeList nodes = current.getElementsByTagName(tags[i]);
            if (nodes.getLength() == 0) return null;
            current = (Element) nodes.item(0);
        }
        NodeList finalNodes = current.getElementsByTagName(tags[tags.length - 1]);
        if (finalNodes.getLength() > 0) {
            return finalNodes.item(0).getTextContent().trim();
        }
        return null;
    }

    private String getTagValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0) return null;
        return nodeList.item(0).getTextContent().trim();
    }
}
