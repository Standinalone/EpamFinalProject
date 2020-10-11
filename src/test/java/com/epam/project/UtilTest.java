package com.epam.project;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.epam.project.entity.Course;
import com.epam.project.util.Page;
import com.epam.project.util.QueryFactory;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.*;

public class UtilTest {
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Mock
	BiFunction<Integer, Integer, List<Course>> getList;
	@Mock
	IntSupplier getCount;

	@Test
	public void getCoursesPage() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(getList.apply(anyInt(), anyInt())).thenReturn(new ArrayList<>());
		when(getCount.getAsInt()).thenReturn(3);
		Page<Course> page = new Page<>(request, getList, getCount);
		assertEquals(0, page.getList().size());
	}

	@Test
	public void getCoursesPageWithParameters() {
		Page<Course> page;
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(getList.apply(anyInt(), anyInt())).thenReturn(new ArrayList<>());
		when(getCount.getAsInt()).thenReturn(3);
		when(request.getParameter("pagenum")).thenReturn("1");
		page = new Page<>("pagenum", "startIndex", "totalPages", request, getList, getCount);
		assertEquals(0, page.getList().size());
	}

	@Test
	public void getCoursesPageWithBadIndex() {
		Page<Course> page;
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(getList.apply(anyInt(), anyInt())).thenReturn(new ArrayList<>());
		when(getCount.getAsInt()).thenReturn(3);
		when(request.getParameter(anyString())).thenReturn("badIndex");
		page = new Page<>("pagenum", "startIndex", "totalPages", request, getList, getCount);
		assertEquals(null, page.getList());
	}
	
	@Test
	public void formConditionQuery() {
		Map<String, Integer> map = new HashMap<>();
		map.put("param1", 1);
		map.put("param2", 2);
		assertEquals("param1=1 AND param2=2", QueryFactory.formExtraConditionQuery(map));
	}
	
	@Test
	public void formOrderByQuery() {
		Map<String, String> COLUMN_NAMES = new HashMap<>();
		COLUMN_NAMES.put("status", "statuses.name");
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getParameter("sort")).thenReturn("status").thenReturn("badsql");
		when(request.getParameter("order")).thenReturn("DESC");
		
		assertEquals(" ORDER BY statuses.name DESC", QueryFactory.formOrderByQuery(COLUMN_NAMES, request));
		assertEquals("", QueryFactory.formOrderByQuery(COLUMN_NAMES, request));
	}

}
