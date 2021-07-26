package test.t5m2

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class TestUserRole implements Serializable {

	private static final long serialVersionUID = 1

	TestUser testUser
	Role role

	@Override
	boolean equals(other) {
		if (other instanceof TestUserRole) {
			other.testUserId == testUser?.id && other.roleId == role?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (testUser) {
            hashCode = HashCodeHelper.updateHash(hashCode, testUser.id)
		}
		if (role) {
		    hashCode = HashCodeHelper.updateHash(hashCode, role.id)
		}
		hashCode
	}

	static TestUserRole get(long testUserId, long roleId) {
		criteriaFor(testUserId, roleId).get()
	}

	static boolean exists(long testUserId, long roleId) {
		criteriaFor(testUserId, roleId).count()
	}

	private static DetachedCriteria criteriaFor(long testUserId, long roleId) {
		TestUserRole.where {
			testUser == TestUser.load(testUserId) &&
			role == Role.load(roleId)
		}
	}

	static TestUserRole create(TestUser testUser, Role role, boolean flush = false) {
		def instance = new TestUserRole(testUser: testUser, role: role)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(TestUser u, Role r) {
		if (u != null && r != null) {
			TestUserRole.where { testUser == u && role == r }.deleteAll()
		}
	}

	static int removeAll(TestUser u) {
		u == null ? 0 : TestUserRole.where { testUser == u }.deleteAll() as int
	}

	static int removeAll(Role r) {
		r == null ? 0 : TestUserRole.where { role == r }.deleteAll() as int
	}

	static constraints = {
	    testUser nullable: false
		role nullable: false, validator: { Role r, TestUserRole ur ->
			if (ur.testUser?.id) {
				if (TestUserRole.exists(ur.testUser.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['testUser', 'role']
		version false
	}
}
