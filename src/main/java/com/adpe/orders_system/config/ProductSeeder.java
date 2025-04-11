package com.adpe.orders_system.config;
import com.adpe.orders_system.DTO.CustomQuery;
import com.adpe.orders_system.DTO.Product;
import com.adpe.orders_system.repository.ProductMongoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class ProductSeeder {

    @Bean
    CommandLineRunner seedProducts(ProductMongoRepository productRepository) {
        return args -> {
            // Verificar si ya hay productos en la base de datos
            CustomQuery query = new CustomQuery();// va a ser un query vacio para obtener todos los productos
            List<Product> existingProducts = productRepository.getMany(query);
           
            if (existingProducts.size() == 0) {
                System.out.println("No products found in the database. Seeding default products...");

                // Generar 20 productos con datos realistas
                List<Product> products = new ArrayList<>();
                products.add(new Product( "Laptop Dell XPS 13", "Ultrabook de alto rendimiento", "SKU-001", 15, 1200.99));
                products.add(new Product( "Smartphone Samsung Galaxy S23", "Teléfono inteligente de última generación", "SKU-002", 25, 999.99));
                products.add(new Product( "Auriculares Sony WH-1000XM5", "Auriculares inalámbricos con cancelación de ruido", "SKU-003", 30, 349.99));
                products.add(new Product( "Monitor LG UltraGear 27\"", "Monitor gaming 4K con 144Hz", "SKU-004", 10, 499.99));
                products.add(new Product( "Teclado Mecánico Logitech G Pro", "Teclado mecánico RGB para gaming", "SKU-005", 20, 129.99));
                products.add(new Product( "Mouse Razer DeathAdder V2", "Mouse ergonómico para gaming", "SKU-006", 50, 69.99));
                products.add(new Product( "Tablet Apple iPad Air", "Tablet ligera y potente con pantalla Retina", "SKU-007", 18, 599.99));
                products.add(new Product( "Cámara Canon EOS R50", "Cámara mirrorless para fotografía profesional", "SKU-008", 8, 899.99));
                products.add(new Product( "Smartwatch Garmin Fenix 7", "Reloj inteligente para deportes y aventuras", "SKU-009", 12, 699.99));
                products.add(new Product( "Disco Duro Externo WD 2TB", "Almacenamiento portátil de alta capacidad", "SKU-010", 40, 89.99));
                products.add(new Product( "Silla Gaming Secretlab Titan", "Silla ergonómica para largas sesiones de juego", "SKU-011", 5, 449.99));
                products.add(new Product( "Impresora HP LaserJet Pro", "Impresora láser compacta y eficiente", "SKU-012", 10, 199.99));
                products.add(new Product( "Router TP-Link Archer AX50", "Router Wi-Fi 6 de alta velocidad", "SKU-013", 25, 129.99));
                products.add(new Product( "Cargador Portátil Anker 20000mAh", "Power bank de alta capacidad", "SKU-014", 35, 49.99));
                products.add(new Product( "Altavoz Bluetooth JBL Charge 5", "Altavoz portátil resistente al agua", "SKU-015", 22, 179.99));
                products.add(new Product( "Microondas Samsung 23L", "Microondas con grill y tecnología avanzada", "SKU-016", 8, 149.99));
                products.add(new Product( "Refrigerador LG InstaView", "Refrigerador inteligente con puerta de cristal", "SKU-017", 3, 1999.99));
                products.add(new Product( "Cafetera Nespresso Vertuo", "Cafetera automática para cápsulas", "SKU-018", 15, 249.99));
                products.add(new Product( "Aspiradora Dyson V15 Detect", "Aspiradora inalámbrica con detección de polvo", "SKU-019", 7, 749.99));
                products.add(new Product( "Televisor Samsung QLED 55\"", "Televisor 4K con tecnología QLED", "SKU-020", 6, 1299.99));
                

                // Guardar los productos en la base de datos
                for (Product product : products) {
                    productRepository.create(product);
                }
                System.out.println("20 products have been added to the database.");
            } else {
                System.out.println("Products already exist in the database. No seeding required.");
            }
        };
    }
}