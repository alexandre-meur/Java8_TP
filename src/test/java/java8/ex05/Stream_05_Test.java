package java8.ex05;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Exercice 5 - Files
 */
public class Stream_05_Test {

    // Chemin vers un fichier de données des naissances
    private static final String NAISSANCES_DEPUIS_1900_CSV = "./naissances_depuis_1900.csv";

    private static final String DATA_DIR = "./pizza-data";


    // Structure modélisant les informations d'une ligne du fichier
    class Naissance {
        String annee;
        String jour;
        Integer nombre;

        public Naissance(String annee, String jour, Integer nombre) {
            this.annee = annee;
            this.jour = jour;
            this.nombre = nombre;
        }
        
        public Naissance(String line) {
        	String[] data = line.split(";");
        	boolean splitSuccess = data.length == 4 && !data[1].equals("ANNEE");
        	
        	if(splitSuccess) {
            	setAnnee(data[1]);
            	setJour(data[2]);
            	try {
            		setNombre(Integer.valueOf(data[3]));
            	}catch(Exception e){
            		System.out.println(e.getMessage());
            		splitSuccess = false;
            	}
        	}else if(!splitSuccess){
        		setAnnee("1900");
            	setJour("19000101");
            	setNombre(0);
        	}
        }
        
        public String toString() {
        	return "A: "+annee+" J:"+jour+" nb:"+nombre;
        }

        public String getAnnee() {
            return annee;
        }

        public void setAnnee(String annee) {
            this.annee = annee;
        }

        public String getJour() {
            return jour;
        }

        public void setJour(String jour) {
            this.jour = jour;
        }

        public Integer getNombre() {
            return nombre;
        }

        public void setNombre(Integer nombre) {
            this.nombre = nombre;
        }
    }
    
    @Test
    public void test() throws IOException {
    	 try {
    		 Stream<String> lines = Files.lines(FileSystems.getDefault().getPath("", NAISSANCES_DEPUIS_1900_CSV));
    		 
    		 lines.map(l -> new Naissance(l))
    		 .filter(n -> Integer.parseInt(n.getAnnee()) < 1902)
    			.forEach(n -> System.out.println(n.toString()));
    	 }catch(Exception e) {
    		 System.out.println(e.getMessage());
    	 }
    }


    @Test
    public void test_group() throws IOException {

        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(FileSystems.getDefault().getPath("", NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO construire une MAP (clé = année de naissance, valeur = somme des nombres de naissance de l'année)
            Map<String, Integer> result = lines
            		.map(Naissance::new)
            		.collect(Collectors.groupingBy(Naissance::getAnnee,
            				Collectors.summingInt(Naissance::getNombre)));

            lines.close();
            assertThat(result.get("2015"), is(8097));
            assertThat(result.get("1900"), is(5130));
        }
    }

    @Test
    public void test_max() throws IOException {
        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(FileSystems.getDefault().getPath("", NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO trouver l'année où il va eu le plus de nombre de naissance
            Optional<Naissance> result = lines
            		.map(Naissance::new)
            		.collect(Collectors.maxBy(Comparator.comparingInt(Naissance::getNombre)));


            assertThat(result.get().getNombre(), is(48));
            assertThat(result.get().getJour(), is("19640228"));
            assertThat(result.get().getAnnee(), is("1964"));
        }
    }

    @Test
    public void test_collectingAndThen() throws IOException {
        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(FileSystems.getDefault().getPath("", NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO construire une MAP (clé = année de naissance, valeur = maximum de nombre de naissances)
            // TODO utiliser la méthode "collectingAndThen" à la suite d'un "grouping"
            Map<String, Naissance> result = null;
            				
            		

            assertThat(result.get("2015").getNombre(), is(38));
            assertThat(result.get("2015").getJour(), is("20150909"));
            assertThat(result.get("2015").getAnnee(), is("2015"));

            assertThat(result.get("1900").getNombre(), is(31));
            assertThat(result.get("1900").getJour(), is("19000123"));
            assertThat(result.get("1900").getAnnee(), is("1900"));
        }
    }

    // Des données figurent dans le répertoire pizza-data
    // TODO explorer les fichiers pour voir leur forme
    // TODO compléter le test

    @Test
    public void test_pizzaData() throws IOException {
        // TODO utiliser la méthode java.nio.file.Files.list pour parcourir un répertoire

        // TODO trouver la pizza la moins chère
        String pizzaNamePriceMin = null;

        assertThat(pizzaNamePriceMin, is("L'indienne"));

    }

    // TODO Optionel
    // TODO Créer un test qui exporte des données new Data().getPizzas() dans des fichiers
    // TODO 1 fichier par pizza
    // TODO le nom du fichier est de la forme ID.txt (ex. 1.txt, 2.txt)

}