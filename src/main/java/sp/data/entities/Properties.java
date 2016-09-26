package sp.data.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


@Entity
@Table(name="properties")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Properties {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="percent_sp")
	private int percenrSp;

	@Column(name="percent_discount")
	private int percenrDiscount;

	@Column(name="percent_bank_comission")
	private int percenrBankComission;


	public Properties() {
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPercenrSp() {
		return percenrSp;
	}

	public void setPercenrSp(int percenrSp) {
		this.percenrSp = percenrSp;
	}

	public int getPercenrDiscount() {
		return percenrDiscount;
	}

	public void setPercenrDiscount(int percenrDiscount) {
		this.percenrDiscount = percenrDiscount;
	}

	public int getPercenrBankComission() {
		return percenrBankComission;
	}

	public void setPercenrBankComission(int percenrBankComission) {
		this.percenrBankComission = percenrBankComission;
	}
}
