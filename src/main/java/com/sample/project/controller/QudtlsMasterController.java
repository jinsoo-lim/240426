package com.sample.project.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sample.project.entity.QudtlsMaster;
import com.sample.project.service.QudtlsMasterService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class QudtlsMasterController {

	LocalDate now = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	String formattedDate = now.format(formatter);

	@Autowired
	private QudtlsMasterService qudtlsMasterService;

	@GetMapping("/download/csv")
	public void downloadCSV(HttpServletResponse response) throws IOException {
		response.setContentType("text/csv; charset=UTF-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"List_" + formattedDate + ".csv\"");

		response.getWriter().write("\uFEFF");

		List<QudtlsMaster> qudtlsemf = qudtlsMasterService.getAllQudtls();

		response.getWriter().write("ID,Name,Age,Job,Rank,Reason\n");

		for (QudtlsMaster qudtls : qudtlsemf) {
			response.getWriter()
					.write(String.format("%d,%s,%d,%s,%s,%s\n", qudtls.getId(), qudtls.getName(),
							qudtls.getAge(), qudtls.getJob(), qudtls.getRank(),
							qudtls.getReason()));
		}

		response.getWriter().flush();
		response.getWriter().close();
	}

	@GetMapping("/qudtlsMaster")
	public String listQudtls(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<QudtlsMaster> qudtlsPage = qudtlsMasterService.findPaginated(page, size);

		model.addAttribute("qudtlsemf", qudtlsPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", qudtlsPage.getTotalPages());
		model.addAttribute("totalItems", qudtlsPage.getTotalElements());

		return "qudtlsMaster";
	}

	@GetMapping("/qudtls/details/{id}")
	public String showDetails(@PathVariable("id") Long id, Model model) {
		QudtlsMaster qudtls = qudtlsMasterService.getQudtlsById(id);
		model.addAttribute("qudtls", qudtls);
		return "qudtlsDetails"; // The name of your Thymeleaf template for the details page
	}

}
