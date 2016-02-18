package org.lucci.lmu;

import org.lucci.lmu.AssociationRelation.TYPE;

public class DeploymentUnitRelation extends Relation {
	
	private TYPE type = TYPE.ASSOCIATION;
	private String cardinality;
	private String label;

	public DeploymentUnitRelation(DeploymentUnit tail, DeploymentUnit head) {
		super(tail, head);
	}

    public String getLabel()
	{
		return label;
	}
	public void setLabel(String label)
	{
		this.label = label;
	}
	public TYPE getType()
	{
		return type;
	}
	public void setType(TYPE type)
	{
		if (type == null)
			throw new NullPointerException();
		
		this.type = type;
	}
	public String getCardinality()
	{
		return cardinality;
	}
	public void setCardinality(String cardinality)
	{
		this.cardinality = cardinality;
	}
	
	public DeploymentUnit getContainerEntity()
	{
		return (DeploymentUnit) getHeadEntity();
	}
	
	public void setContainerEntity(DeploymentUnit e)
	{
		setHeadEntity(e);
	}
	
	public DeploymentUnit getContainedEntity()
	{
		return (DeploymentUnit) getTailEntity();
	}

	public void setContainedEntity(DeploymentUnit e)
	{
		setTailEntity(e);
	}

}
