package com.ppe.TennisTieBreak.planification;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppe.TennisTieBreak.sessionPlanifiee.SessionPlanifiee;

@Service
public class PlanificationService implements IPlanificationService {

	@Autowired
	private PlanificationRepository planificationRepository;

	@Override
	public Planification save(Planification planification) {
		return this.planificationRepository.save(planification);

	}

	@Override
	public Planification edit(Planification planification) {
		return this.planificationRepository.save(planification);

	}

	@Override
	public void deleteById(Long id) {
		if (getById(id) != null) {
			this.planificationRepository.deleteById(id);
		}
	}

	@Override
	public Planification getById(Long id) {
		return this.planificationRepository.getById(id);
	}

	@Override
	public List<Planification> getAll() {
		return this.planificationRepository.findAll();
	}

	@Override
	public List<SessionPlanifiee> PlanfierSession(Planification planification) {

		return null;
	}

	public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate, String dow) {
		List<LocalDate> selectedDay = new ArrayList<>();
		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		numOfDaysBetween++;
		// System.out.println(numOfDaysBetween);
		List<LocalDate> days = IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween)
				.mapToObj(i -> startDate.plusDays(i)).collect(Collectors.toList());
		for (int i = 0; i < days.size(); i++) {
			if (dow.isEmpty()) {
				selectedDay.add(days.get(i));
			} else {
				String day = String.valueOf(DayOfWeek.from(days.get(i)).getValue());
				// debug data
				// System.out.println(dow +"-" +DayOfWeek.from(days.get(i)).getValue() + "-" +
				// "\"" + dow.indexOf(day) + "\"");
				if (dow.indexOf(day) > -1)
					selectedDay.add(days.get(i));
			}
		}
		return selectedDay;
	}

}
