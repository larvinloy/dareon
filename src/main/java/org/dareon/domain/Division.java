package org.dareon.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

@Entity
@Inheritance
@DiscriminatorValue("DIVISION")
public class Division extends anzsrc
{

}
