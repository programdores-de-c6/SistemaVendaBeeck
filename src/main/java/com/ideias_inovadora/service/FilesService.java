package com.ideias_inovadora.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ideias_inovadora.DirectoryInitializer;
import com.ideias_inovadora.model.Employee;
import com.ideias_inovadora.model.FileCategory;
import com.ideias_inovadora.model.Files;
import com.ideias_inovadora.model.Product;
import com.ideias_inovadora.model.Shop;
import com.ideias_inovadora.repository.FilesRepository;

@Service
public class FilesService {
	
	@Autowired
	FilesRepository filesRepository;
	@Autowired
	private DirectoryInitializer directoryInitializer;
	
	 public Files salvarArquivoFoto(MultipartFile arquivo, FileCategory categoria) throws Exception {
	    	validacao(arquivo);
	    	Files files= salvarpasta(arquivo, categoria);
	    	//filesRepository.deletefoto(utilizador.getId());
	    	return 	filesRepository.save(files);
			
	    }

	    public String deletarFoto(long id) throws Exception {
//	        filesRepository.deletefoto(id);
	        return "Deletado com sucesso";
	    }

	    

		
	   
	    public Files salvarpasta(MultipartFile arquivo, FileCategory categoria)
				throws IllegalStateException, IOException {
	    	Files file = new Files();
	    	directoryInitializer.initDirectories();
			String ProjectoDirectory = System.getProperty("user.dir") +  File.separator  +"upload";
			
			String categoriaDir = ProjectoDirectory + File.separator + categoria.name().toLowerCase();
			
			// GARANTIR QUE AS PASTAS EXISTEM
		    File dir = new File(categoriaDir);
		    if (!dir.exists()) {
		        dir.mkdirs(); 
		    }
			
			// Gera um nome único para o arquivo
			String nomeUnico = UUID.randomUUID().toString() + "_"
					+ arquivo.getOriginalFilename();
			
		
			
			//String caminhoCompleto = ProjectoDirectory + "/" + nomeUnico;
			//caminhoCompleto = caminhoCompleto.replace("\\", "/"); // Substitui as barras invertidas pelas barras normais
			
			// Salva o arquivo no diretório
		    File destino = new File(dir, nomeUnico);
			arquivo.transferTo(destino);
			// Cria uma instância de FileLegislacao e salva no banco de dados
			String formato = arquivo.getOriginalFilename()
					.substring(arquivo.getOriginalFilename().lastIndexOf(".") + 1);
			
			file.setSize( (int) (arquivo.getSize() / 1024.0));
			file.setTipo(formato);
			file.setCaminho(destino.getAbsolutePath());
			file.setNome(arquivo.getOriginalFilename());
			file.setData(LocalDateTime.now());
			return file;
		}
	    
	    public void validacao(MultipartFile arquivo ) throws Exception {

			long limiteTamanho = 5 * 1024 * 1024; // 5MB

			

			String nomeOriginal = arquivo.getOriginalFilename(); // Obter o nome original do arquivo

			if (arquivo.isEmpty()) {
				throw new IllegalArgumentException("Arquivo inválido: " + nomeOriginal);
			}

			if (!nomeOriginal.toLowerCase().endsWith(".png") && !nomeOriginal.toLowerCase().endsWith(".jpg")) {
				throw new Exception("Formato da Foto não suportado: " + nomeOriginal);
			}

			if (arquivo.getSize() > limiteTamanho) {
				throw new IllegalArgumentException("Tamanho máximo da foto excedida. O tamanho máximo permitido é de 5MB.");
			}

		}
	    
	 // 🔹 Novo método: Buscar arquivo pelo ID e retornar bytes
	    public byte[] buscarArquivo(long id) throws IOException {
	    	Files files = filesRepository.findById(id)
	    	        .orElseThrow(() -> new IllegalArgumentException("Arquivo não encontrado"));

	        if (files == null) {
	            throw new IllegalArgumentException("Arquivo não encontrado para o ID: " + id);
	        }

	        File arquivoFisico = new File(files.getCaminho());
	        if (!arquivoFisico.exists()) {
	            throw new IllegalArgumentException("Arquivo físico não encontrado: " + files.getCaminho());
	        }

	        // Retorna o conteúdo do arquivo como bytes
	        // Lê o conteúdo do arquivo usando java.nio.file.Files
	        return java.nio.file.Files.readAllBytes(arquivoFisico.toPath());
	    }
	    
	    
	    public String buscarArquivoBase64(long id) throws IOException {
	    	Files files = filesRepository.findById(id)
	    	        .orElseThrow(() -> new IllegalArgumentException("Arquivo não encontrado"));

	        if (files == null) throw new IllegalArgumentException("Arquivo não encontrado");
	        
	        File arquivoFisico = new File(files.getCaminho());
	        byte[] conteudo = java.nio.file.Files.readAllBytes(arquivoFisico.toPath());
	        return Base64.getEncoder().encodeToString(conteudo);
	    }

	
		public String atualizarArquivoFoto(MultipartFile file, Shop shop, FileCategory categoria) throws Exception {
			validacao(file);
			Files files = salvarpasta(file, categoria);
			files.setId(shop.getFiles().getId());
			filesRepository.saveAndFlush(files);
			return "Operação realizada com sucesso";
		}

		public String atualizarArquivoFotos(MultipartFile file, Product product, FileCategory categoria) throws Exception {
			validacao(file);
			Files files = salvarpasta(file, categoria);
			files.setId(product.getFiles().getId());
			filesRepository.saveAndFlush(files);
			return "Operação realizada com sucesso";
		}

		public String atualizarArquivoFotos(MultipartFile file, Employee employee, FileCategory categoria)throws Exception  {
			validacao(file);
			Files files = salvarpasta(file, categoria);
			files.setId(employee.getFiles().getId());
			filesRepository.saveAndFlush(files);
			return "Operação realizada com sucesso";
			
		}

		
}
